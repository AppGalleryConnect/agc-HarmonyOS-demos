/**
 * News list item info.
 */
export class NewsData {
  id: number;
  /**
   * News list item title.
   */
  title: string;

  /**
   * News list item content.
   */
  content: string;

  /**
   * News list item imagesUrl.
   */
  imagesUrl: Array<NewsFile>;

  /**
   * News list item source.
   */
  source: string;

  constructor(id: number, title: string, content: string, imagesUrl: Array<NewsFile>, source: string) {
    this.id=id;
    this.title = title;
    this.content = content;
    this.imagesUrl = imagesUrl;
    this.source = source;
  }
}

/**
 * News image list item info.
 */
export class NewsFile {
  /**
   * News image list item id.
   */
  id: number;

  /**
   * News image list item url.
   */
  url: string;

  /**
   * News image list item type.
   */
  type: number;

  /**
   * News image list item newsId.
   */
  newsId: number;

  constructor(id: number, url: string, type: number, newsId: number) {
    this.id = id;
    this.type = type;
    this.url = url;
    this.newsId = newsId;
  }
}