import React from 'react';
import {Root, View, Panel, PanelHeader, Div, Spinner} from "@vkontakte/vkui";
import { Trans } from 'react-i18next';
import pageEmitter from "../emitters/pages_emitter";
import MenuBlock from "./menu_block";
import ChatsRecommended from "./chats/recommended";
import ChatsAll from "./chats/all";
import UserInfo from "./user_info";
import UsersCommonInterests from "./users/common";

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
      page : PAGE_INIT
    };

    //  Делаем подписку на события
    pageEmitter.onPageChanged((page) => this.onPageChanged(page));
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
   * Рендер компонента
   * @returns {*}
   */
  render() {
    return (
      <Root activeView={this.state.page}>
        <View id={PAGE_INIT} activePanel={PAGE_INIT + '1'}>
          <Panel id={PAGE_INIT + '1'}>
            <PanelHeader><Trans>Init</Trans></PanelHeader>
            <Div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
              <Spinner size="large" style={{ marginTop: 20 }} />
            </Div>
          </Panel>
        </View>

        <View id={PAGE_CHATS_RECOMMENDED} activePanel={PAGE_CHATS_RECOMMENDED + '1'}>
          <Panel id={PAGE_CHATS_RECOMMENDED + '1'}>
            <PanelHeader><Trans>Recommended Chat Title</Trans></PanelHeader>
            <MenuBlock pageId={PAGE_CHATS_RECOMMENDED} />

            <ChatsRecommended />
          </Panel>
        </View>

        <View id={PAGE_CHATS_ALL} activePanel={PAGE_CHATS_ALL + '1'}>
          <Panel id={PAGE_CHATS_ALL + '1'}>
            <PanelHeader><Trans>All Chats Title</Trans></PanelHeader>
            <MenuBlock pageId={PAGE_CHATS_ALL} />

            <ChatsAll />
          </Panel>
        </View>

        <View id={PAGE_USER_INFO} activePanel={PAGE_USER_INFO + '1'}>
          <Panel id={PAGE_USER_INFO + '1'}>
            <PanelHeader><Trans>User Info Title</Trans></PanelHeader>
            <MenuBlock pageId={PAGE_USER_INFO} />

            <UserInfo />
          </Panel>
        </View>

        <View id={PAGE_USERS_COMMON} activePanel={PAGE_USERS_COMMON + '1'}>
          <Panel id={PAGE_USERS_COMMON + '1'}>
            <PanelHeader><Trans>Users Common Interests Title</Trans></PanelHeader>
            <MenuBlock pageId={PAGE_USERS_COMMON} />

            <UsersCommonInterests />
          </Panel>
        </View>
      </Root>
    );
  }
}

export default MainComponent;