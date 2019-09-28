import React from 'react';
import {Group, Cell, Avatar, View, Panel} from "@vkontakte/vkui";
import userInfo from "../../../helper/user_info";

const COMPONENT_NAME = 'UserInfo';

/**
 * Компонент с инфой по юзеру
 */
class UserInfo extends React.Component {
  /**
   * Рендер приложения
   * @returns {*}
   */
  render() {
    return (
      <View id={COMPONENT_NAME + 'View'} activePanel={COMPONENT_NAME + 'Panel'}>
        <Panel id={COMPONENT_NAME + 'Panel'}>
          <Group title="User Info">
            <Cell
              photo={userInfo.getUser().avatar}
              description="Ваши интересы: 1,2,3"
              before={<Avatar src={userInfo.getUser().avatar} size={80}/>}
              size="l"
            >
              {userInfo.getUser().first_name + ' ' + userInfo.getUser().last_name}
            </Cell>
          </Group>
        </Panel>
      </View>
    );
  }
}

export default UserInfo;