import React from 'react';
import {
  Root,
  View,
  Panel,
  PanelHeader,
  Div,
  Spinner,
  ModalCard,
  FormLayout,
  FormLayoutGroup,
  Link, ModalRoot
} from "@vkontakte/vkui";
import { Trans } from 'react-i18next';
import pageEmitter from "../emitters/pages_emitter";
import MenuBlock from "./menu_block";
import ChatsRecommended from "./chats/recommended";
import ChatsAll from "./chats/all";
import UserInfo from "./user_info";
import UsersCommonInterests from "./users/common";
import authEmitter from "../emitters/auth_emitter";

//  Страница инициализации
export const PAGE_INIT = 'init';
//  Страница с рекомендуемыми чатами
export const PAGE_CHATS_RECOMMENDED = 'chats_recommended';
//  Страница со всеми чатами
export const PAGE_CHATS_ALL = 'chats_all';
//  Страница с инфой по юзеру
export const PAGE_USER_INFO = 'user_info';
//  Страница с юзерами похожими интересами
export const PAGE_USERS_COMMON = 'users_common';

const ROOT_VIEW_LOADING = 'root_view_loading';
const ROOT_VIEW_PAGE = 'root_view_page';

/**
 * Основной компонент приложения
 */
class MainComponent extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);

    this.state = {
      page : PAGE_INIT,
      rootView: ROOT_VIEW_LOADING,
      authFailed: false
    };

    //  Делаем подписку на события
    pageEmitter.onPageChanged((page) => this.onPageChanged(page));
    authEmitter.subscribeOnAuth(() => this.showPages());
    authEmitter.subscribeOnSignInFailed(() => this.showAuthFailedModal());
  }

  /**
   * Обработчик смены страницы
   * @param page
   */
  onPageChanged(page) {
    switch(page) {
      case PAGE_CHATS_RECOMMENDED: this.setChatsRecommenedPage(); break;
      case PAGE_CHATS_ALL: this.setChatsAllPage(); break;
      case PAGE_USER_INFO: this.setUserInfoPage(); break;
      case PAGE_USERS_COMMON: this.setUsersCommonPage(); break;

      default: break;
    }
  }

  /**
   * Отображает страницы
   */
  showPages() {
    this.setState({rootView: ROOT_VIEW_PAGE});
  }

  /**
   * Отображает модальное окно авторизации
   */
  showAuthFailedModal() {
    this.setState({authFailed: true});
  }

  /**
   * Задает главную страницу
   */
  setChatsRecommenedPage() {
    this.setState({
      page : PAGE_CHATS_RECOMMENDED
    });
  }

  /**
   * Задает страницу все чаты
   */
  setChatsAllPage() {
    this.setState({
      page : PAGE_CHATS_ALL
    });
  }

  /**
   * Задает страницу инфа пользователя
   */
  setUserInfoPage() {
    this.setState({
      page : PAGE_USER_INFO
    });
  }

  /**
   * Задает страницу с пользователями у которых похожие интересы
   */
  setUsersCommonPage() {
    this.setState({
      page: PAGE_USERS_COMMON
    });
  }

  /**
   * Отрисовывает процесс загрузки
   * @returns {*}
   */
  renderLoading() {
    const display = (this.state.authFailed) ? (<h1>Авторизация провалилась</h1>) : (<Spinner size="large" style={{ marginTop: 20 }} />);

    return (
      <Div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
        {display}
      </Div>
    );
  }

  /**
   * Отрисовка страницы
   * @returns {*}
   */
  renderPage() {
    switch(this.state.page) {
      case PAGE_CHATS_RECOMMENDED: return (<ChatsRecommended />);
      case PAGE_CHATS_ALL: return (<ChatsAll />);
      case PAGE_USER_INFO: return (<UserInfo />);
      case PAGE_USERS_COMMON: return (<UsersCommonInterests />);
      default: break;
    }
  }

  /**
   * Рендер компонента
   * @returns {*}
   */
  render() {
    return (
      <Root activeView={this.state.rootView}>
        <View id={ROOT_VIEW_LOADING} activePanel={ROOT_VIEW_LOADING + '1'}>
          <Panel id={ROOT_VIEW_LOADING + '1'}>
            <PanelHeader><Trans>App Name</Trans></PanelHeader>
            { this.renderLoading() }
          </Panel>
        </View>
        <View id={ROOT_VIEW_PAGE} activePanel={ROOT_VIEW_PAGE + '1'}>
          <Panel id={ROOT_VIEW_PAGE + '1'}>
            <PanelHeader><Trans>App Name</Trans></PanelHeader>
            <MenuBlock pageId={this.state.page} />

            {this.renderPage()}
          </Panel>
        </View>
      </Root>
    );
  }
}

export default MainComponent;