//
//  Реализация доступа к бэкенду
//

import {backendUrl} from "../../environments/backend_config";

const GET = 'get';
const POST = 'post';

class BackendApi {
  /**
   * Возвращает рекомендованные чаты
   * @returns {Promise<any>}
   */
  getRecommendedChats(token, user_id, info) {
    return new Promise((resolve, reject) => {
      this._sendRequest(backendUrl + "/getRecommended", POST, {
        vk_id: user_id,
        vk_token: token,
        ip: "127.0.0.1",
        info: info
      })
        .then(chats => resolve(chats))
        .catch(err => reject(err));
    });
  }

  /**
   * Возвращает все чаты
   * @param page
   * @param offset
   * @returns {Promise<any>}
   */
  getAllChats(page = 0, offset = -1) {
    return new Promise((resolve, reject) => {
      this._sendRequest(backendUrl + "/getAll?page=" + page + "&start_id=" + offset, GET)
        .then(chats => resolve(chats))
        .catch(err => reject(err));
    });
  }

  /**
   * Возвращает похожих пользователей
   * @param vk_id
   * @param page
   * @param offset
   * @returns {Promise<any>}
   */
  getLikeUser(vk_id, page = 0, offset = -1) {
    return new Promise((resolve, reject) => {
      this._sendRequest(backendUrl + "/getLikeUser?vk_id=" + vk_id + "&page=" + page + "&start_id=" + offset, GET)
        .then(users => resolve(users))
        .catch(err => reject(err));
    });
  }

  /**
   * Возвращает ссылку на чат
   * @param chat_id
   * @returns {Promise<any>}
   */
  getChatLink(chat_id) {
    return new Promise((resolve, reject) => {
      this._sendRequest(backendUrl + "/getChatLink?chat_id=" + chat_id, GET)
        .then(link => resolve(link))
        .catch(err => reject(err));
    });
  }

  /**
   * Отсылает запрос и возвращает промис в виде JSON
   * @param url
   * @param method
   * @param data
   * @returns {Promise<any | never>}
   * @private
   */
  _sendRequest(url, method, data = null) {
    switch(method) {
      case POST:
        return fetch(url, {
          method: method,
          headers: {'Content-Type':'application/json'},
          body: JSON.stringify(data)
        }).then(res => res.json());
      case GET:
        return fetch(url, {
          method: method,
          headers: {'Content-Type':'application/json'}
        }).then(res => res.json());
      default: break;
    }
  }
}

const backendApi = new BackendApi();
export default backendApi;