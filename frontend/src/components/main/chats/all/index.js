import React from 'react';
import {Panel, View} from "@vkontakte/vkui";

const COMPONENT_NAME = 'ChatsAll';

/**
 * Компонент со всеми чатами
 */
class ChatsAll extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);
    this.state = {
      chats: []
    };
  }

  /**
   * Рендер приложения
   * @returns {*}
   */
  render() {
    return (
      <View id={COMPONENT_NAME + 'View'} activePanel={COMPONENT_NAME + 'Panel'}>
        <Panel id={COMPONENT_NAME + 'Panel'}>

        </Panel>
      </View>
    );
  }
}

export default ChatsAll;