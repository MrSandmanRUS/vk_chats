import React from 'react';
import {Root, View, Panel, PanelHeader, Div, Spinner} from "@vkontakte/vkui";
import { Trans } from 'react-i18next';
import pageEmitter from "../emitters/pages_emitter";

//  Страница инициализации
export const PAGE_INIT = 'init';
//  Страница с рекомендуемыми чатами
export const PAGE_CHATS_RECOMMENDED = 'chats_recommended';
//  Страница со всеми чатами
export const PAGE_CHATS_ALL = 'chats_all';
//  Страница с инфой по юзеру
export const PAGE_USER_INFO = 'user_info';

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
            <h1>Test</h1>
          </Panel>
        </View>

        <View id={PAGE_CHATS_ALL} activePanel={PAGE_CHATS_ALL + '1'}>
          <Panel id={PAGE_CHATS_ALL + '1'}>

          </Panel>
        </View>

        <View id={PAGE_USER_INFO} activePanel={PAGE_USER_INFO + '1'}>
          <Panel id={PAGE_USER_INFO + '1'}>

          </Panel>
        </View>
      </Root>
    );
  }
}

export default MainComponent;