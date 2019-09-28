/**
 * Класс доступа к данным пользователя
 */
class UserInfo {
  /**
   * Конструктор класса
   */
  constructor() {
    this._user = {
      first_name: '',
      last_name: '',
      avatar: '',
      city: '',
      country: '',
      bday: ''
    };
    this._groups = [];
  }

  /**
   * Возвращает массив с данными пользователя
   * @returns {boolean}
   */
  getUser() {
    return this._user;
  }

  /**
   * Возвращает группы пользователя
   * @returns {Array}
   */
  getGroups() {
    return this._groups;
  }

  /**
   * Задает данные пользователя
   * @param data
   */
  setUserData(data) {
    if (data.first_name) this._user.first_name = data.first_name;
    if (data.last_name) this._user.last_name = data.last_name;
    if (data.photo_max_orig) this._user.avatar = data.photo_max_orig;
    if (data.city) this._user.city = data.city.title;
    if (data.country) this._user.country = data.country.title;
    if (data.bday) this._user.bday = data.bdate;
  }

  /**
   * Задает группы пользователя
   * @param data
   */
  setGroupsData(data) {
    this._groups = [];


  }
}

const userInfo = new UserInfo();
export default userInfo;