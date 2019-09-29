import React from 'react';
import {Avatar, Cell, Group, List, Panel, PullToRefresh, View, Spinner, Link} from "@vkontakte/vkui";
import userInfo from "../../../../helper/user_info";
import backendApi from "../../../../api/backend";
import {Trans} from "react-i18next";

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
    this.state = {
      users: [],
      fetching: false,
      firstInit: true
    };
    this.onRefresh = () => {this.updateUsers();}
  }

  /**
   * Вызывается при подключении компонента к дереву
   */
  componentDidMount() {
    //  Шлем к бэку когда компонет подключился к дереву
    this.updateUsers();
  }

  /**
   * Обновляет список пользователей
   */
  updateUsers() {
    this.setState({
      fetching: true
    });

    backendApi.getLikeUser(userInfo.getUser().id)
      .then(users => {
        for(let i = 0; i < users.length; i++) {
          users[i].info = JSON.parse(users[i].info);
          users[i].link = 'https://vk.com/id' + users[i].id;
          users[i].name = users[i].info.first_name + users[i].info.last_name;
        }
        this.setState({
          users: users,
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
   * Отрисовка списка пользователей
   */
  renderUsers() {
    if (!this.state.users.length) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
          <Cell multiline>
            <h1 style={{textAlign: 'center'}}>Не удалось найти людей со схожими интересами</h1>
          </Cell>
        </div>
      );
    }

    return this.state.users.map(({ id, avatar, name, link }, i) =>
      <Cell key={i}
            before={<Avatar src={avatar} />}
            onClick={() => window.open(link)}
      >
        {name} <br /> <Link href={link} target={'_blank'}><Trans>Profile</Trans></Link>
      </Cell>
    );
  }

  /**
   * Метод отрисовки
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
                  {this.renderUsers()}
                </List>
              </Group>
            </PullToRefresh>
          </Group>
        </Panel>
      </View>
    );
  }
}

export default UsersCommonInterests;