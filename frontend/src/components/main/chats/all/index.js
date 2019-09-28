import React from 'react';
import {Avatar, Cell, Group, Link, List, Panel, PullToRefresh, Spinner, View} from "@vkontakte/vkui";
import backendApi from "../../../../api/backend";
import {Trans} from "react-i18next";

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
   * Обновляет список чатов
   */
  updateChats() {
    this.setState({
      fetching: true
    });

    backendApi.getAllChats()
      .then(chats => {
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

export default ChatsAll;