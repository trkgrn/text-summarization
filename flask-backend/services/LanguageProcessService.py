from utils import GloveUtil


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
