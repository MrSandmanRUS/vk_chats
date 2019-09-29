import connect from '@vkontakte/vk-connect';
import {VK_API_VERSION, VK_ID, VK_SCOPE} from "../../environments/vk_config";

/**
 * Доступ к апи вк
 */
class VkApi {
  /**
   * Конструктор класса
   */
  constructor() {
    //  Токен доступа
    this._accessToken = '';
    //  Инициализируем логгер на события ВК
    connect.send("VKWebAppInit", {});
  }

  /**
   * Возвращает токен доступа
   * @returns {string}
   */
  getAccessToken() {
    return this._accessToken;
  }

  /**
   * Задает токен доступа
   * @param token
   */
  setAccessToken(token) {
    this._accessToken = token;
  }

  /**
   * Возвращает промис на авторизацию
   * @returns {Promise<ReceiveData<string>>}
   */
  authUser() {
    return new Promise((resolve, reject) => {
      this._sendMethod('VKWebAppGetAuthToken', {
        "app_id": VK_ID,
        "scope": VK_SCOPE
      })
        .then(data => resolve(data))
        .catch(err => reject(err));
    });
  }

  /**
   * Возвращает промис на получение данных пользователя
   * @returns {Promise<any>}
   */
  getUserInfo() {
    return new Promise((resolve, reject) => {
      this._sendMethod('VKWebAppGetUserInfo', {})
        .then(data => resolve(data))
        .catch(err => reject(err));
    });
  }

  /**
   * Возвращает группы пользователя
   * @returns {Promise<any>}
   */
  getUserGroups(count = 1000) {
    return new Promise((resolve, reject) => {
      if (this._accessToken === '') reject('Not auth');
      else {
        this._sendMethod('VKWebAppCallAPIMethod', {
          "method": "groups.get",
          "request_id": "getUserGroups",
          "params": {
            "extended": 1,
            "v": VK_API_VERSION,
            "access_token": this._accessToken,
            "count": count
          }
        })
          .then(data => resolve(data))
          .catch(err => reject(err));
      }
    });
  }

  /**
   * Отсылает запрос к ВК и возвращает промис
   * @param methodName
   * @param data
   * @returns {Promise<ReceiveData<*>>}
   * @private
   */
  _sendMethod(methodName, data) {
    return connect.sendPromise(methodName, data);
  }
}

const vkApi = new VkApi();
export default vkApi;