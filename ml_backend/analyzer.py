from pymorphy2 import MorphAnalyzer
import nltk
import re
import heapq
from collections import Counter

with open('stopwords.txt', 'rt', encoding='utf-8') as file:
    input_str = file.read()
    stopwords = input_str.split(',')
morphy_analyzer = MorphAnalyzer()


def most_chats(json_obj, numb_top):
    input_txt = json_obj
    text = ' '.join(input_txt)
    formatted_text = re.sub('[^a-zа-яА-ЯA-Zё]', ' ', text)
    formatted_text = re.sub(r'\b\w\b', ' ', formatted_text)
    formatted_text = re.sub(r'\s+', ' ', formatted_text)
    filtered_groups = filter(lambda x: x not in stopwords,
                             nltk.word_tokenize(formatted_text.lower()))
    filtered_groups = filter(lambda x: len(x) > 3, filtered_groups)
    filtered_groups = (morphy_analyzer.parse(i)[0].normal_form for i in filtered_groups)

    frequent_words = Counter(filtered_groups)

    topic_for_chat = heapq.nlargest(numb_top, frequent_words, key=frequent_words.get)

    return topic_for_chat


def generate_synonyms(top_charts, model, numb_syn):
    generated_words = []
    for word in top_charts:
        try:
            synoms = [i for i, j in model.most_similar(word, topn=numb_syn)]
            generated_words.extend(synoms)
        except KeyError:
            continue
    return generated_words


def predict(model, user_groups, final_numb_topic=5, init_numb_top=5, numb_syn=10):
    if not user_groups:
        top_charts = most_chats(user_groups, init_numb_top)
        list_gen_words = generate_synonyms(top_charts, model, numb_syn)
        list_gen_words.extend(top_charts)
        topics = most_chats(list_gen_words, final_numb_topic)
    else:
        topics = ['аноним']
    return topics