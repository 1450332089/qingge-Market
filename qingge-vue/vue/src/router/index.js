import Vue from 'vue'
import VueRouter from 'vue-router'
import request from '../utils/request';


Vue.use(VueRouter)
//requireAuth: 是否需要检查登录
const routes = [
    //前台
  {
    path: '/',
    name: 'front',
    redirect: "/topview",
    component: () => import('../views/front/Front.vue'),
    meta: {title:'清歌商城', path: '清歌商城', requireAuth: false},
    children: [
      {path: 'person', name: 'person', meta: {title:'个人信息',requireLogin: true}, component: () => import('../views/Person.vue'),},
      {path: 'topview', name: 'topview', meta: {title:'清歌商城'}, component: () => import('../views/front/TopView.vue'),},
      {path: 'cart', name: 'cart', meta: {title:'我的购物车',requireLogin: true}, component: () => import('../views/front/good/Cart.vue'),},
      {path: 'goodList', name: 'goodList', meta: {title:'商品界面'}, component: () => import('../views/front/good/GoodList.vue'),},
      {path: 'goodView/:goodId', name: 'goodview', meta: {title:'商品详情'}, component: () => import('../views/front/good/GoodView.vue'),},
      {path: 'schoolMarket', name: 'schoolMarket', meta: {title:'校园集市'}, component: () => import('../views/front/market/SchoolMarket.vue'),},
      {path: 'marketGood/:goodId', name: 'marketGood', meta: {title:'集市商品'}, component: () => import('../views/front/market/MarketGoodView.vue'),},
      {path: 'addMarketGood', name: 'addMarketGood', meta: {title:'发布商品',requireLogin: true}, component: () => import('../views/front/market/AddGood.vue'),},
      {path: 'myGood', name: 'myGood', meta: {title:'我的发布',requireLogin: true}, component: () => import('../views/front/market/MyGood.vue'),},
      {path: 'editGood/:goodId', name: 'editGood', meta: {title:'编辑商品',requireLogin: true}, component: () => import('../views/front/market/EditGood.vue'),},
      {path: 'preOrder', name: 'preOrder', meta: {title:'确认订单',requireLogin: true}, component: () => import('../views/front/order/PreOrder.vue'),},
      {path: 'pay', name: 'pay', meta: {title:'支付',requireLogin: true}, component: () => import('../views/front/order/Pay.vue'),},
      {path: 'orderList', name: 'orderList', meta: {title:'我的订单',requireLogin: true}, component: () => import('../views/front/order/OrderList.vue'),},

    ]
  },
    //后台
  {
    path: '/manage',
    name: 'manage',
    component: () => import('../views/manage/Manage.vue'),
    redirect: "/manage/home",
    meta: {title:'后台', path: '后台',requireAuth: true},
    children: [
      {path: 'home', name: 'home', meta: {title:'主页', path: '主页',requireAuth: true}, component: () => import('../views/manage/Home.vue'),},
      {path: 'user', name: 'user', meta: {title:'用户管理',path: '系统管理/用户管理',requireAuth: true}, component: () => import('../views/manage/User.vue'),},
      {path: 'person', name: 'person', meta: {title:'个人信息',path: '个人信息',requireAuth: true}, component: () => import('../views/Person.vue'),},
      {path: 'file', name: 'file', meta: {title:'文件管理',path: '文件/文件管理',requireAuth: true}, component: () => import('../views/manage/file/File.vue'),},
      {path: 'avatar', name: 'avatar', meta: {title:'头像管理',path: '文件/头像管理',requireAuth: true}, component: () => import('../views/manage/file/Avatar.vue'),},
      {path: 'carousel', name: 'carousel', meta: {title:'轮播图管理',path: '商品/轮播图管理',requireAuth: true}, component: () => import('../views/manage/good/Carousel.vue'),},
      {path: 'category', name: 'category', meta: {title:'商品分类管理',path: '商品/商品分类管理',requireAuth: true}, component: () => import('../views/manage/good/Category.vue'),},
      {path: 'good', name: 'good', meta: {title:'商品管理',path: '商品/商品管理',requireAuth: true}, component: () => import('../views/manage/good/Goods.vue'),},
      {path: 'goodInfo', name: 'goodInfo', meta: {title:'商品管理',path: '商品/商品管理/商品信息',requireAuth: true}, component: () => import('../views/manage/good/GoodInfo.vue'),},
      {path: 'order', name: 'order', meta: {title:'订单管理',path: '商品/订单管理',requireAuth: true}, component: () => import('../views/manage/Order.vue'),},
      {path: 'incomeChart', name: 'incomeChart', meta: {title:'收入图表',path: '营收/收入图表',requireAuth: true}, component: () => import('../views/manage/income/IncomeChart.vue'),},
      {path: 'incomeRank', name: 'incomeRank', meta: {title:'收入排行',path: '营收/收入排行',requireAuth: true}, component: () => import('../views/manage/income/IncomeRank.vue'),},

    ]
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      requireAuth: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/register',
    name: 'register',
    meta: {
      title: '注册',requireAuth: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/Register.vue')
  },
  {
    path: '/*',
    name: 'notFound',
    meta: {
      title: '找不到页面'
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/404NotFound.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//beforeEach是router的钩子函数，在进入路由前执行
router.beforeEach((to, from, next) => {
  // if(to.path === '/manage'){
  //   let user = localStorage.getItem("user");
  //   if(!user.token){
  //     next('/login');
  //   }
  // }
  let role;
  let allow = false;
  if(to.meta.requireAuth===true){
    //在后台获得该用户的身份
    request.post("http://localhost:8888/role").then(res=>{
      if(res.code==='200'){
        role = res.data;
        console.log('您的身份是：'+role);
        if(role === 'admin'){
          allow = true;
        }
        else if(role==='user'){
            alert("您没有权限");
            allow = false;
            next("/")
        }
      }
      else{  //查询身份失败
        alert(res.msg);
        next('/login');
      }
      //放行
      if(allow === true){
        //设置网页title
        if (to.meta.title) {
          document.title = to.meta.title
        } else {
          document.title ='未知页面'
        }
        next()
      }
    }
    )
  }
  else{    //不需要判断权限
    if(to.meta.requireLogin===true){
      if(!isLogin()){
        next('/login');
      }
    }
    if (to.meta.title) {
      document.title = to.meta.title
    } else {
      document.title ='未知页面'
    }
    next()
  }

})

function isLogin() {
  let user = localStorage.getItem("user");
  if(user){
    return true;
  }else{
    return false;
  }
}
export default router
