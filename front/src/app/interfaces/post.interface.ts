export interface Post {
    id: number;
    title: string;
    content: string;
    authorUsername: string;
    topicName: string;
    createdAt: Date;
}

export interface PostComment {
    id: number;
    authorUsername: string;
    content: string;
    createdAt: Date;
}

/** Étend Post avec la liste des commentaires
 * utilisé sur la page de détail d'un article. 
 */
export interface PostDetail extends Post {
    comments: PostComment[];
}