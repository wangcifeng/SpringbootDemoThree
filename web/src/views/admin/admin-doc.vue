<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-button v-model:value="param.name" type="primary" @click="handleQuery()" size="large">查询</a-button>
          </a-form-item>
          <a-form-item>
            <a-button v-model:value="param.name" type="primary" @click="add(record)" size="large">新增</a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table :columns="columns" :row-key="record => record.id" :data-source="level1" :loading="loading"
        :pagination="false">
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 50px; height: 50px;" />
        </template>
        <template v-slot:action="{ record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">编辑</a-button>
            <a-popconfirm title="删除后不可恢复，确认删除吗?" ok-text="是" cancel-text="否" @confirm="deleteHandle(record.id)">
              <a-button type="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal title="文档表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleOk">

    <a-form :model="doc" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select ref="select" v-model:value="doc.parent">
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">{{c.name}}</a-select-option>
        </a-select>

      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool'

export default defineComponent({
  name: 'AdminDoc',
  setup() {

    const param = ref();
    param.value = {};
    const columns = [
      {
        title: 'name',
        dataIndex: 'name',
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent',
      },
      {
        title: '顺序',
        dataIndex: 'sort',
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];

    const docs = ref();

    const loading = ref(false);

    /**
     * 数据查询查询
     */
    const level1 = ref();//一级分类树，children是二级分类树
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        console.log(response, '2222');
        if (data.success) {
          docs.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);

        } else {
          message.error(data.message);
        }
      });
    }

    /**
     * 模态框--表单
     */
    const doc = ref({}); //每一条数据的点击按钮表单
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data
        if (data.success) {
          modalVisible.value = false;

          //重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
    * 编辑按钮
    */
    const edit = (record) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);
    };

    /**
     * 新增方法
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {};
    };

    /**
    * 删除方法
    */
    const deleteHandle = (id) => {
      axios.delete("/doc/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          //重新加载列表
          handleQuery();
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      docs,
      columns,
      loading,
      doc,
      param,
      level1,

      edit,
      handleOk,
      add,
      deleteHandle,
      handleQuery,

      modalVisible,
      modalLoading
    }
  },
})
</script>

