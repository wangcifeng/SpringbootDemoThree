<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: '0', minHeight: '280px' }">
            <a-row>
                <a-col :span="6">
                    <a-tree v-if="level1.length > 0" :tree-data="level1" @select="onSelect"
                        :replaceFields="{ title: 'name', key: 'id', value: 'id' }" :defaultExpandAll="true"
                        :defaultExpandAllRows="true">
                    </a-tree>
                </a-col>
                <a-col :span="18">col-12</a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>
<script>
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool'
import { useRoute } from 'vue-router';

export default defineComponent({
    name: 'Doc',
    setup() {
        const route = useRoute();

        const docs = ref();
        const loading = ref(false);
        const level1 = ref();//一级分类树，children是二级分类树
        level1.value = [];
        /**
         * 数据查询查询
         */
        const handleQuery = () => {
            loading.value = true;
            //如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
            level1.value = [];
            axios.get("/doc/all").then((response) => {
                loading.value = false;
                const data = response.data;
                if (data.success) {
                    docs.value = data.content;

                    level1.value = [];
                    level1.value = Tool.array2Tree(docs.value, 0);

                } else {
                    message.error(data.message);
                }
            });
        };

        onMounted (() => {
            handleQuery();
        });

        return {
            level1,
            handleQuery

        }
    },
})
</script>
