from pymorphy2 import MorphAnalyzer
import nltk
import re
import heapq
from itertools import combinations
from collections import Counter
from .nltk_levenshtein import perform_corpus

with open('stopwords.txt', 'rt', encoding='utf-8') as file:
    input_str = file.read()
    stopwords = input_str.split(',')
morphy_analyzer = MorphAnalyzer()


def check_substring(a, b):
    if b.find(a) != -1 or a.find(b) != -1:
        return True
    else:
        return False


def del_shortest_word(iter_obj):
    return min(iter_obj, key=len)


def filter_verbs(word):
    # del verbs
    if re.search('.*?[аиеёоуыэюя]ть(ся)?', word):
        if word.find('подслуш') != -1:
            return 'подслушано'
        return ''
    else:
        return word


def most_chats(json_obj, numb_top, final_output):
    text = ' '.join(json_obj)
    formatted_text = re.sub('[^a-zа-яА-ЯA-Zё]', ' ', text)
    formatted_text = re.sub(r'\b\w\b', ' ', formatted_text)
    formatted_text = re.sub(r'\s+', ' ', formatted_text)
    filtered_groups = filter(lambda x: x not in stopwords,
                             nltk.word_tokenize(formatted_text.lower()))
    filtered_groups = filter(lambda x: len(x) > 2, filtered_groups)
    filtered_groups = (morphy_analyzer.parse(i)[0].normal_form for i in filtered_groups)
    filtered_groups = filter(lambda x: x not in stopwords, filtered_groups)
    filtered_groups = map(filter_verbs, filtered_groups)
    # filtered_groups = list(filter(lambda x: len(x) > 1, map(filter_verbs, filtered_groups)))
    # filtered_groups = perform_corpus(filtered_groups, 0.6)
    frequent_words = Counter(filtered_groups)

    if final_output:
        cognate_drop_words = list(map(del_shortest_word, filter(lambda x: check_substring(*x),
                                                                combinations(frequent_words.keys(), 2))))
        frequent_words = dict((k, v) for k, v in frequent_words.items() if k not in cognate_drop_words)
    topic_for_chat = heapq.nlargest(numb_top, frequent_words, key=frequent_words.get)
    return topic_for_chat


def generate_synonyms(top_charts, model, numb_syn):
    generated_words = []
    for word in top_charts:
        try:
            generated_words.extend([i for i, j in model.most_similar(word, topn=numb_syn)])
        except KeyError:
            continue
    return generated_words


def predict(model, user_groups, final_numb_topic=5, init_numb_top=5, numb_syn=10):
    if user_groups:
        top_charts = most_chats(user_groups, init_numb_top, False)
        list_gen_words = generate_synonyms(top_charts, model, numb_syn)
        list_gen_words.extend(top_charts)
        topics = most_chats(list_gen_words, final_numb_topic, True)
    else:
        topics = ['аноним']

    return topics
