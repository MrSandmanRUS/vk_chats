import React from 'react';
import {Group, Cell, Button, Avatar, View, Panel} from "@vkontakte/vkui";
import userInfo from "../../../helper/user_info";

/**
 * Компонент с инфой по юзеру
 */
class UserInfo extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);
  }

  /**
   * Рендер приложения
   * @returns {*}
   */
  render() {
    return (
      <View id={'UserInfoView'} activePanel={'UserInfoPanel'}>
        <Panel id={'UserInfoPanel'}>
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