<template>
  <Tabs value="name1" :animated="false" style="padding:1% 3%;">
    <TabPane label="组织列表" name="name1" />
    <TabPane label="职务列表" name="name2" />
    <TabPane label="岗位列表" name="name3">
      <p>从整体角度概览配置岗位，关联相关用户</p>
      <ListLoader :id="120" />
    </TabPane>
    <TabPane label="工作群组列表" name="name4">
      <p>从群组分类的角度概览配置群组，关联相关用户</p>
      <ListLoader :id="120" />
    </TabPane>
    <TabPane label="任职列表" name="name5">
      <p>反映组织、职务和岗位的用户视图</p>
      <ListLoader :id="120" />
    </TabPane>
  </Tabs>
</template>

<script setup>
import { ListLoader } from '@ajax-js/configurable-widget';
</script>

<style scoped>
p {
  padding-bottom: 1%;
  color: gray;
}
</style>
