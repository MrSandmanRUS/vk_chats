/**
 * Класс обработки событий
 */
class EventEmitter {
  /**
   * Конструктор класса
   */
  constructor() {
    this.events = {};
  }

  /**
   * Рассылает указанное событие
   * @param event
   * @param data
   */
  emit(event, data = null) {
    //  Если подписанных событий нету
    if (!this.events[event]) return;
    //  Иначе прогоняемся по ним
    this.events[event].forEach(callback => callback(data));
  }

  /**
   * Делает подписку на событие
   * @param event
   * @param callback
   */
  subscribe(event, callback) {
    if (!this.events[event]) this.events[event] = [];
    this.events[event].push(callback);
  }
}

export default EventEmitter;