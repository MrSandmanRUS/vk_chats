import React from 'react';
import ReactDOM from 'react-dom';

import './index.css';
import './i18n';

import MainComponent, {PAGE_CHATS_RECOMMENDED} from './components/main';
import vkApi from './api/vk_api';
import * as serviceWorker from './serviceWorker';
import authEmitter from "./components/emitters/auth_emitter";
import pageEmitter from "./components/emitters/pages_emitter";
import userInfo from "./helper/user_info";

ReactDOM.render(<MainComponent />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();

let tries = 0;

const authUserFunction = () => {
  vkApi.authUser()
    .then(data => {
      vkApi.setAccessToken(data.access_token);

      vkApi.getUserInfo()
        .then(data => {
          userInfo.setUserData(data);

          vkApi.getUserGroups()
            .then((groups) => {
              userInfo.setGroupsData(groups);

              authEmitter.emitAuthSuccess();
              pageEmitter.emitPageChanged(PAGE_CHATS_RECOMMENDED);
            })
            .catch(err => {
              console.error(err);
              authEmitter.emitAuthFailed();
            });
        })
        .catch(err => {
          console.error(err);
          authEmitter.emitAuthFailed();
        });
    })
    .catch(err => {
      console.error(err);
      authEmitter.emitAuthFailed();
    });
};

authEmitter.subscribeOnAuthFailed(() => {
  console.log(tries);
  if (tries === 3) {
    alert('Авторизация провалилась!');
  } else {
    tries++;
    authUserFunction();
  }
});

//  На старте приложения посылаем авторизацию пользователя
authUserFunction();