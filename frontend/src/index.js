import React from 'react';
import ReactDOM from 'react-dom';

import './index.css';
import './i18n';

import MainComponent from './components/main';
import vkApi from './api/vk_api';
import * as serviceWorker from './serviceWorker';
import authEmitter from "./components/emitters/auth_emitter";

ReactDOM.render(<MainComponent />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();

//  На старте приложения посылаем авторизацию пользователя
vkApi.authUser()
  .then(data => {
    vkApi.setAccessToken(data.token);
    authEmitter.emitAuthSuccess();
  })
  .catch(err => {
    console.error(err);
    authEmitter.emitAuthFailed();
  });