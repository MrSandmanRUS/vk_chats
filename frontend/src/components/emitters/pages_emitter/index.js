import EmitterProto from "../prototype";

const EMITTER_NAME = 'PagesEmitter';

const PAGE_CHANGED = 'PageChanged';

/**
 * Сообщатель смены страниц
 */
class PagesEmitter extends EmitterProto {
  /**
   * Конструктор класса
   */
  constructor() {
    super(EMITTER_NAME);
  }

  /**
   * Сообщает что страница была изменена
   * @param page
   */
  emitPageChanged(page) {
    this.emit(PAGE_CHANGED, page);
  }

  /**
   * Подписка на смену страниц
   * @param callback
   */
  onPageChanged(callback) {
    this.on(PAGE_CHANGED, callback);
  }
}

const pageEmitter = new PagesEmitter();
export default pageEmitter;