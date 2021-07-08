import Vue from 'vue'
import Router from 'vue-router'

import UserRegistForm from '@/pages/user/RegistForm'
import UserLoginForm from '@/pages/user/LoginForm'
import DocumentCommon from '@/pages/document/Common'
import DocumentRegistForm from '@/pages/document/RegistForm'
import DocumentList from '@/pages/document/List'
import DocumentDetail from '@/pages/document/Detail'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/document',
      component: DocumentCommon,
      children: [
        {
          path: 'detail',
          component: DocumentDetail
        },
        {
          path: 'list',
          component: DocumentList
        },
        {
          path: 'registform',
          component: DocumentRegistForm
        },
      ]
    },
    {
      path: '/user/registform',
      component: UserRegistForm
    },
    {
      path: '/',
      component: UserLoginForm
    },
  ]
})
