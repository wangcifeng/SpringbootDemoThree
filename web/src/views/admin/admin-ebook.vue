<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <a-table :columns="columns" :row-key="record => record.id" :data-source="ebooks" :pagination="pagination"
        :loading="loading" @change="handleTableChange">
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 50px; height: 50px;" />
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">编辑</a-button>
            <a-button type="danger">删除</a-button>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal title="Title" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleOk">

    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="wrapperCol">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" />
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: 'name',
        dataIndex: 'name',
      },
      {
        title: '分类一',
        dataIndex: 'category1Id',
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
      }, {
        title: '阅读数',
        dataIndex: 'viewCount',
      }, {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];

    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });

    const loading = ref(false);

    /**
     * 数据查询查询
     */
    const handleQuery = (params) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

        //重置分页
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    }

    const handleTableChange = (pagination) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    }

    /**
     * 模态框--表单
     */
    const ebook = ref({}); //每一条数据的点击按钮表单
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleOk = () => {
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };

    /**
    * 编辑按钮
    */
    const edit = (record) => {
      modalVisible.value = true;
      ebook.value = record;
    };


    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      ebook,

      handleTableChange,
      edit,
      handleOk,

      modalVisible,
      modalLoading
    }
  },
})
</script>

