<template>
    <h2 style="margin-left: 10px;">登录日志</h2>
    <ListLoader :id="120" :showEditBtn="false" />
</template>

<script>
import { ListLoader } from '@ajax-js/configurable-widget';

export default {
    components: {
        ListLoader
    }
}
</script>
