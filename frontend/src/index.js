import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import MainComponent from './components/main';
import vkApi from './api/vk_api';
import * as serviceWorker from './serviceWorker';

vkApi.authUser()
  .then(data => console.log(data))
  .catch(err => console.error(err));

ReactDOM.render(<MainComponent />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
