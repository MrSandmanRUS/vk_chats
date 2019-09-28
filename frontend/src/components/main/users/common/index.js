import React from 'react';
import {Avatar, Cell, Group, Panel, View} from "@vkontakte/vkui";

const COMPONENT_NAME = 'UsersCommonInterests';

/**
 * Пользователи с похожими интересами
 */
class UsersCommonInterests extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);
  }

  /**
   * Метод отрисовки
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

export default UsersCommonInterests;