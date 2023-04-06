<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>

            <a-input v-model:value="param.name" placeholder="名称" size="large"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button v-model:value="param.name" type="primary"
              @click="handleQuery({ page: 1, size: pagination.pageSize })" size="large">查询</a-button>

          </a-form-item>
          <a-form-item>
            <a-button v-model:value="param.name" type="primary" @click="add(record)" size="large">新增</a-button>

          </a-form-item>

        </a-form>
      </p>

      <a-table :columns="columns" :row-key="record => record.id" :data-source="ebooks" :pagination="pagination"
        :loading="loading" @change="handleTableChange">
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 50px; height: 50px;" />
        </template>

        <!-- <template v-slots:category="{ text,record }">
          <span>{{getCategoryName(record.category1Id)}} / {{getCategoryName(record.category2Id)}}</span>
        </template> -->

        <template v-slot:category="{ record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>

        <template v-slot:action="{ record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">文档管理</a-button>
            </router-link>

            <a-button type="primary" @click="edit(record)">编辑</a-button>

            <a-popconfirm title="删除后不可恢复，确认删除吗?" ok-text="是" cancel-text="否" @confirm="deleteHandle(record.id)">
              <a-button type="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal title="电子书管理表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleOk">

    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="wrapperCol">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <!-- <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" /> -->
        <a-cascader v-model:value="categoryIds" :options="level1"
          :field-names="{ label: 'name', value: 'id', children: 'children' }" />
      </a-form-item>
      <a-form-item label="文档数">
        <a-input v-model:value="ebook.docCount" />
      </a-form-item>
      <a-form-item label="阅读数">
        <a-input v-model:value="ebook.viewCount" />
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
import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool'

export default defineComponent({
  name: 'AdminEbook',
  setup() {

    const param = ref();
    param.value = {};
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
      // {
      //   title: '分类一',
      //   dataIndex: 'category1Id',
      // },
      // {
      //   title: '分类二',
      //   dataIndex: 'category2Id',
      // },
      {
        title: '分类一',
        slots: { customRender: 'category' }
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
      pageSize: 10,
      total: 0
    });

    const loading = ref(false);

    /**
     * 数据查询查询
     */
    const handleQuery = (params) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;

          //重置分页
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
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
    const categoryIds = ref();
    const ebook = ref(); //每一条数据的点击按钮表单
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data
        if (data.success) {
          modalVisible.value = false;

          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
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
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    /**
     * 新增方法
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    /**
    * 删除方法
    */
    const deleteHandle = (id) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    const level1 = ref();//一级分类树，children是二级分类树
    let categorys;
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      ebook,
      param,
      categoryIds,
      level1,

      handleTableChange,
      edit,
      handleOk,
      add,
      deleteHandle,
      handleQuery,
      getCategoryName,

      modalVisible,
      modalLoading
    }
  },
})
</script>

