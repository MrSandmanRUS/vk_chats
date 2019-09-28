import React from 'react';
import {Panel, View, PullToRefresh, Cell, Avatar, Group, List, Spinner, Link} from "@vkontakte/vkui";
import backendApi from "../../../../api/backend";
import vkApi from "../../../../api/vk_api";
import userInfo from "../../../../helper/user_info";
import {Trans} from "react-i18next";

const COMPONENT_NAME = 'ChatsRecommended';

/**
 * Компонент с рекомендуемыми чатами
 */
class ChatsRecommended extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);
    this.state = {
      chats: [],
      fetching: false,
      firstInit: true
    };
    this.onRefresh = () => {this.updateChats();}
  }

  /**
   * Вызывается при подключении компонента к дереву
   */
  componentDidMount() {
    //  Шлем к бэку когда компонет подключился к дереву
    this.updateChats();
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
   * Обновляет список чатов
   */
  updateChats() {
    this.setState({
      fetching: true
    });

    backendApi.getRecommendedChats(
      vkApi.getAccessToken(),
      userInfo.getUser().id,
      this.generateInfo()
    )
      .then((chats) => {
        this.setState({
          chats: chats,
          fetching: false,
          firstInit: false
        });
      })
      .catch(err => alert(err));
  }

  /**
   * Отрисовывает спиннер старта
   * @returns {*}
   */
  renderStartSpinner() {
    if (this.state.firstInit) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
          <Spinner size="large" style={{ marginTop: 20 }} />
        </div>
      )
    }
  }

  /**
   * Вызывает отрисовку чата
   * @returns {*[]}
   */
  renderChats() {
    return this.state.chats.map(({ id, interest, preview, link }, i) =>
      <Cell key={i}
            before={<Avatar src={preview} />}
            onClick={() => window.open(link)}
      >
        <Trans>Interests</Trans>: {interest} <br /> <Link href={link} target={'_blank'}><Trans>Conversation</Trans></Link>
      </Cell>
    );
  }

  /**
   * Рендер приложения
   * @returns {*}
   */
  render() {
    return (
      <View id={COMPONENT_NAME + 'View'} activePanel={COMPONENT_NAME + 'Panel'}>
        <Panel id={COMPONENT_NAME + 'Panel'}>
          <Group>
            { this.renderStartSpinner() }

            <PullToRefresh onRefresh={this.onRefresh} isFetching={this.state.fetching}>
              <Group>
                <List>
                  { this.renderChats() }
                </List>
              </Group>
            </PullToRefresh>
          </Group>
        </Panel>
      </View>
    );
  }
}

export default ChatsRecommended;