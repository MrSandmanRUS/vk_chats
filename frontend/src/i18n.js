//
//  Локализация
//

import i18n from "i18next";
import { initReactI18next } from "react-i18next";

//  Описываем переводы
const resources = {
  ru: {
    translation: {
      "Init": "Инициализация приложения",
      "Recommended Chat Title" : "Рекомендованные чаты",
      "All Chats Title" : "Все чаты",
      "User Info Title" : "Информация о пользователе"
    }
  }
};

//  Инициализируем переводы
i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: "ru",
    keySeparator: false,
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;