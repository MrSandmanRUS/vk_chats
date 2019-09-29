import EventEmitter from "../../../helper/event_emitter";

/**
 * Прототип эмиттеров компонентов
 */
class EmitterProto {
  /**
   * Конструктор класса
   * @param title
   */
  constructor(title) {
    this._title = title + ':';
    this._emitter = new EventEmitter();
  }

  /**
   * Подписка на событие
   * @param event
   * @param callback
   */
  on(event, callback) {
    this._emitter.subscribe(this._getEventName(event), callback);
  }

  /**
   * Рассылка сообщений
   * @param event
   * @param data
   */
  emit(event, data) {
    this._emitter.emit(this._getEventName(event), data);
  }

  /**
   * Возвращает имя ивента с учетом эмитерра
   * @param event
   * @returns {*}
   * @private
   */
  _getEventName(event) {
    return this._title + event;
  }
}

export default EmitterProto;