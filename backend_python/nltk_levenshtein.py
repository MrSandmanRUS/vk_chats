from nltk import edit_distance
from itertools import combinations


def _adv_edit_distance(s1, s2):
    return 1 - edit_distance(s1, s2) / len(max(s1, s2, key=len))


def _corpus_comb_to_leven(comb_pairs):
    return ((x, _adv_edit_distance(*x)) for x in comb_pairs)


def _make_pairs(tuple_elem):
    return max(tuple_elem, key=len), min(tuple_elem, key=len)


def _make_dict_pairs(comb_pairs_leven, bound):
    pairs_to_trans = filter(lambda tuple_elem: tuple_elem[-1] >= bound, comb_pairs_leven)
    return dict(_make_pairs(pair[0]) for pair in pairs_to_trans)


def perform_corpus(corpus, bound=0.5):
    corpus_of_comb = combinations(corpus, 2)
    comb_pairs_leven = _corpus_comb_to_leven(corpus_of_comb)
    dict_pairs = _make_dict_pairs(comb_pairs_leven, bound)
    return [dict_pairs.get(key, key) for key in corpus]


words = ['питер', 'петербург', 'moscow', 'moscowskiy', 'ball', 'lopata']
print(perform_corpus(words))
