import React from 'react';
import {Group, Cell, Avatar, View, Panel} from "@vkontakte/vkui";
import userInfo from "../../../helper/user_info";
import backendApi from "../../../api/backend";
import vkApi from "../../../api/vk_api";

const COMPONENT_NAME = 'UserInfo';

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

    this.state = {
      interests: '',
      fetching: false,
    };
  }

  /**
   * После маутинга
   */
  componentDidMount() {
    this.getInterests();
  }

  /**
   * Возвращает информационную модель
   * @returns {{country: {id: number; title: string} | Boolean.country | {id: number; name: string} | string | string, city: {id: number; title: string} | Boolean.city | {id: number; name: string} | string | string, last_name: string | Boolean.last_name | string, groups: Array, first_name: string | Boolean.first_name | string, born_date: string}}
   */
  generateInfo() {
    const user = userInfo.getUser();
    const groups_list = userInfo.getGroups();

    let groups = [];
    for(let i = 0; i < groups_list.length; i++) {
      groups.push(groups_list[i].name);
    }

    return {
      first_name: user.first_name,
      last_name: user.last_name,
      avatar: user.avatar,
      born_date: user.bday,
      city: user.city,
      country: user.country,
      groups: groups
    };
  }

  /**
   * Получает интересы текущего пользователя
   */
  getInterests() {
    this.setState({fetching: true});

    backendApi.getRecommendedChats(
      vkApi.getAccessToken(),
      userInfo.getUser().id,
      this.generateInfo()
    )
      .then((chats) => {
        let interests = '';
        for(let i = 0; i < chats.length; i++) {
          interests += chats[i].interest;
          if (i !== (chats.length - 1)) {
            interests += ', ';
          }
        }

        this.setState({
          interests: interests,
          fetching: false,
        });
      })
      .catch(err => alert(err));
  }

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
              description={"Ваши интересы: " + this.state.interests}
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