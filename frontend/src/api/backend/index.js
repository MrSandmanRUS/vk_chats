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
      this._sendRequest(backendUrl + "/getAll?page=" + page + "&offset=" + offset, GET)
        .then(chats => resolve(chats))
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
    return fetch(url, {
      method: method,
      headers: {'Content-Type':'application/json'},
      body: data
    }).then(res => res.json());
  }
}

const backendApi = new BackendApi();
export default backendApi;