import React from 'react';
import {FixedLayout, Tabs, HorizontalScroll, TabsItem} from "@vkontakte/vkui";
import pageEmitter from "../../emitters/pages_emitter";
import {PAGE_CHATS_ALL, PAGE_CHATS_RECOMMENDED, PAGE_USER_INFO} from "../index";
import {Trans} from "react-i18next";

const TAB_RECOMMENDED = 'tab_recommended';
const TAB_ALL = 'tab_all';
const TAB_USER = 'tab_user';

/**
 * Блок меню
 */
class MenuBlock extends React.Component {
  /**
   * Конструктор класса
   * @param props
   */
  constructor(props) {
    super(props);
  }

  /**
   * Задает указанную вкладку
   * @param tab
   */
  setTab(tab) {
    switch (tab) {
      case TAB_RECOMMENDED: pageEmitter.emitPageChanged(PAGE_CHATS_RECOMMENDED); break;
      case TAB_ALL: pageEmitter.emitPageChanged(PAGE_CHATS_ALL); break;
      case TAB_USER: pageEmitter.emitPageChanged(PAGE_USER_INFO); break;
      default: break;
    }
  }

  /**
   * Рендер меню
   * @returns {*}
   */
  render() {
    return (
      <FixedLayout vertical="top">
        <Tabs theme="header" type="buttons">
          <HorizontalScroll>
            <TabsItem
              onClick={() => this.setTab(TAB_RECOMMENDED)}
              selected={this.props.pageId === PAGE_CHATS_RECOMMENDED}
            >
              <Trans>Recommended Chat Title</Trans>
            </TabsItem>
            <TabsItem
              onClick={() => this.setTab(TAB_ALL)}
              selected={this.props.pageId === PAGE_CHATS_ALL}
            >
              <Trans>All Chats Title</Trans>
            </TabsItem>
            <TabsItem
              onClick={() => this.setTab(TAB_USER)}
              selected={this.props.pageId === PAGE_USER_INFO}
            >
              <Trans>User Info Title</Trans>
            </TabsItem>
          </HorizontalScroll>
        </Tabs>
      </FixedLayout>
    );
  }
}

export default MenuBlock;