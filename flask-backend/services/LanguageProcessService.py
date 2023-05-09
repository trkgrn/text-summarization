from utils import GloveUtil, StringUtil


def get_similarities_by_document(document):
    sentences = document['sentences']
    print(len(sentences))

    for s in sentences:
        s1 = s['text']
        if len(s['similarities']) < 1: continue
        for similarity in s['similarities']:
            s2 = similarity['sentence']['text']
            rate = GloveUtil.cosine_distance_wordembedding_method(s1, s2)
            print(rate)
            similarity['similarityRate'] = rate
    return document


def calculate_sentence_score_by_document(document):
    sentences = document['sentences']

    sentence_list = []

    for s in sentences:
        sentence_list.append(s['text'])

    theme_words = StringUtil.get_theme_words_by_sentences(sentence_list)
    title = document['title']
    node_count = sentence_list.__len__()

    for s in sentences:
        s['sentenceScore'] = StringUtil.calculate_sentence_score(s, theme_words, title, node_count)

    return document


def calculate_rouge_score(summary, reference):
    return StringUtil.calculate_rouge_score(summary, reference)
