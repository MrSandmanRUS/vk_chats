//
//  Локализация
//

import i18n from "i18next";
import { initReactI18next } from "react-i18next";

//  Описываем переводы
const resources = {
  ru: {
    translation: {
      "App Name" : "ВЧатике",
      "Init": "Инициализация приложения",
      "Recommended Chat Title" : "Рекомендованные",
      "All Chats Title" : "Все",
      "User Info Title" : "Инфо",
      "Users Common Interests Title" : "Пользователи",
      "Conversation" : "Беседа",
      "Interests" : "Интересы",
      "Profile" : "Открыть профиль"
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