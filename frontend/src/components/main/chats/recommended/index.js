import React from 'react';
import {
  Panel,
  View,
  PullToRefresh,
  Cell,
  Avatar,
  Group,
  List,
  Spinner,
  Link,
  ModalRoot,
  ModalCard, FormLayout, FormLayoutGroup
} from "@vkontakte/vkui";
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
      firstInit: true,
      chatLinkLoading: false,
      modalLink: '',
      modalActive: null
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
   * Отображает модалку
   * @param link
   */
  showChatModal(link) {
    this.setState({modalLink: link, modalActive: 'chatLink'});
  }

  /**
   * Обработчик нажатия чата
   * @param id
   * @param link
   */
  chatClicked(id, link) {
    this.setState({chatLinkLoading: true});

    if (!link) {
      backendApi.getChatLink(id)
        .then(data => {
          this.setState({chatLinkLoading: false});
          this.showChatModal(data.link);
        })
        .catch(err => alert(err));
    } else {
      this.setState({chatLinkLoading: false});
      this.showChatModal(link);
    }
  }

  /**
   * Отрисовывает спиннер старта
   * @returns {*}
   */
  renderStartSpinner() {
    if (this.state.firstInit) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
          <h1>Анализируем</h1>
          <h2>Это займет пару минут...</h2>
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
    if (!this.state.firstInit && !this.state.chatLinkLoading) {
      return this.state.chats.map(({ id, interest, preview, link }, i) =>
        <Cell key={i}
              before={<Avatar src={preview} />}
              onClick={() => this.chatClicked(id, link)}
        >
          <Link>{interest}</Link>
        </Cell>
      );
    }
  }

  /**
   * Вызывает отрисовку загрузки ссылки
   * @returns {*}
   */
  renderChatLink() {
    if (this.state.chatLinkLoading) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
          <h1>Получаем ссылку на чат</h1>
          <h2>Это займет какое-то время...</h2>
          <Spinner size="large" style={{ marginTop: 20 }} />
        </div>
      )
    }
  }

  /**
   * Рендер приложения
   * @returns {*}
   */
  render() {
    const modal = (
      <ModalRoot activeModal={this.state.modalActive}>
        <ModalCard
          id={'chatLink'}
          onClose={() => this.setState({modalActive: null})}
        >
          <FormLayout>
            <FormLayoutGroup>
              <div style={{ display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
                <Link href={this.state.modalLink} target={'_blank'}>Нажмите для перехода к беседе</Link>
              </div>
            </FormLayoutGroup>
          </FormLayout>
        </ModalCard>
      </ModalRoot>
    );

    return (
      <View id={COMPONENT_NAME + 'View'} activePanel={COMPONENT_NAME + 'Panel'} modal={modal}>
        <Panel id={COMPONENT_NAME + 'Panel'}>
          <Group>
            { this.renderStartSpinner() }
            { this.renderChatLink() }

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