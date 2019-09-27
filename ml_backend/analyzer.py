from pymorphy2 import MorphAnalyzer
import nltk
import json
import re
import heapq
from collections import Counter

morphy_analyzer = MorphAnalyzer()
####
with open('../testapi/ml_backend/morf_vk.txt', 'r', encoding='utf-8-sig') as file:
    txt = file.read()
json_obj = json.loads(txt)
input_txt = json_obj['groups']
####
text = ' '.join(input_txt)
formatted_text = re.sub('[^a-zа-яА-ЯA-Zё]', ' ', text)
formatted_text = re.sub(r'\b\w\b', ' ', formatted_text)
formatted_text = re.sub(r'\s+', ' ', formatted_text)
ru_stopwords = nltk.corpus.stopwords.words('russian')
en_stopwords = nltk.corpus.stopwords.words('english')

filtered_groups = filter(lambda x: _ if x in ru_stopwords or en_stopwords else x,
                     nltk.word_tokenize(formatted_text.lower()))
filtered_groups = filter(lambda x: len(x) > 3, filtered_groups)
filtered_groups = (morphy_analyzer.parse(i)[0].normal_form for i in filtered_groups)

frequent_words = Counter(filtered_groups)

topic_for_chat = heapq.nlargest(10, frequent_words, key=frequent_words.get)
print(topic_for_chat)
