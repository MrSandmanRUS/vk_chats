import React from 'react';
import {Avatar, Cell, Group, List, Panel, PullToRefresh, View, Spinner} from "@vkontakte/vkui";
import userInfo from "../../../../helper/user_info";

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
        console.log(users);
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