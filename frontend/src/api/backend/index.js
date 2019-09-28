//
//  Реализация доступа к бэкенду
//

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

    });
  }
}

const backendApi = new BackendApi();
export default backendApi;