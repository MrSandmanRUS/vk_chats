import React from 'react';
import {Root, View, Panel, PanelHeader, Div, Spinner} from "@vkontakte/vkui";
import { Trans } from 'react-i18next';

//  Страница инициализации
const PAGE_INIT = 'init';
//  Главная страница
const PAGE_INDEX = 'index';
//  Страница с рекомендуемыми чатами
const PAGE_CHATS_RECOMMENDED = 'chats_recommended';
//  Страница со всеми чатами
const PAGE_CHATS_ALL = 'chats_all';
//  Страница с инфой по юзеру
const PAGE_USER_INFO = 'user_info';

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
    }
  }

  /**
   * Задает главную страницу
   */
  setIndexPage() {
    this.setState({
      page : PAGE_INDEX
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