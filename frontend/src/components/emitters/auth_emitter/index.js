import EmitterProto from "../prototype";

const EMITTER_NAME = 'AuthEmitter';

const AUTH_SUCCESS = 'AuthSuccess';
const AUTH_FAILED = 'AuthFailed';

/**
 * События по авторизации
 */
class AuthEmitter extends EmitterProto {
  /**
   * Конструктор класса
   */
  constructor() {
    super(EMITTER_NAME);
  }

  /**
   * Подписка на событие авторизации
   * @param callback
   */
  subscribeOnAuth(callback) {
    this.on(AUTH_SUCCESS, callback);
  }

  /**
   * Подписка на событие провальной авторизации
   * @param callback
   */
  subscribeOnAuthFailed(callback) {
    this.on(AUTH_FAILED, callback);
  }

  /**
   * Посылает сообщение о том, что юзер авторизован
   */
  emitAuthSuccess() {
    this.emit(AUTH_SUCCESS);
  }

  /**
   * Посылает сообщение о том, что авторизация провалилась
   */
  emitAuthFailed() {
    this.emit(AUTH_FAILED);
  }
}

const authEmitter = new AuthEmitter();
export default authEmitter;