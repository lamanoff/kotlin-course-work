import os
import pickle
import numpy as np
import pandas as pd

from core.utils import RESOURCE_PATH, question_to_vec, load_embeddings


def get_path_for_file(path):
    return os.path.join(os.path.dirname(os.path.abspath(__file__)), path)


starspace_embeddings, embeddings_dim = load_embeddings(get_path_for_file(RESOURCE_PATH['WORD_EMBEDDINGS']))
os.makedirs(get_path_for_file(RESOURCE_PATH['THREAD_EMBEDDINGS_FOLDER']), exist_ok=True)

posts_df = pd.read_csv(get_path_for_file('data/tagged_posts.tsv'), sep='\t')
counts_by_tag = posts_df.groupby('tag')['tag'].count()
for tag, count in counts_by_tag.items():
    tag_posts = posts_df[posts_df['tag'] == tag]

    tag_post_ids = tag_posts['post_id'].values

    tag_vectors = np.zeros((count, embeddings_dim), dtype=np.float32)
    for i, title in enumerate(tag_posts['title']):
        tag_vectors[i, :] = question_to_vec(title, starspace_embeddings, embeddings_dim)

    # Dump post ids and vectors to a file.
    filename = os.path.join(get_path_for_file(RESOURCE_PATH['THREAD_EMBEDDINGS_FOLDER']),
                            os.path.normpath('%s.pkl' % tag))
    pickle.dump((tag_post_ids, tag_vectors), open(filename, 'wb'))
