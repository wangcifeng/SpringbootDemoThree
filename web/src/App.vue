<template>
 <a-layout>
    <the-header></the-header>
    <router-view></router-view>
    <the-footer></the-footer>
  </a-layout>
</template>

<script>
import { defineComponent } from 'vue'
import theFooter from './components/the-footer.vue'
import theHeader from './components/the-header.vue'

export default defineComponent({
  components: {
    theHeader,
    theFooter
  },
  // setup() {
    
  // },
})
</script>


<style>
#components-layout-demo-top-side .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
