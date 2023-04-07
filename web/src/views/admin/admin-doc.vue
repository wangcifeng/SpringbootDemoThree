<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <a-row :gutter="24">
        <a-col :span="8">
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
          <a-table :columns="columns" :row-key="record => record.id" :data-source="level1" :loading="loading" size="small"
            v-if="level1.length > 0" :defaultExpandAllRows="true" :pagination="false">
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{ record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">编辑</a-button>
                <a-popconfirm title="删除后不可恢复，确认删除吗?" ok-text="是" cancel-text="否" @confirm="deleteHandle(record.id)">
                  <a-button type="danger" size="small">删除</a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleOk()" size="small">保存</a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" :label-col="{ span: 3 }" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" />
            </a-form-item>
            <a-form-item>
              <a-tree-select v-model:value="doc.parent" style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" :tree-data="treeSelectData"
                placeholder="请选择父文档" :replaceFields="{ title: 'name', key: 'id', value: 'id' }" tree-default-expand-all>
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序" />
            </a-form-item>
            <a-form-item>
              <div style="border: 1px solid #ccc">
                <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig"
                  :mode="mode" />
                <Editor style="height: 200px; overflow-y: hidden;" v-model="valueHtml"
                  :defaultConfig="editorConfig" :mode="mode" @onCreated="handleCreated" />
              </div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>


    </a-layout-content>
  </a-layout>

  <!-- <a-modal title="文档表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleOk">
   
  </a-modal> -->
</template>

<script>
import { defineComponent, ref, onMounted, shallowRef, onBeforeUnmount } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/util/tool'
import { useRoute } from 'vue-router';
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default defineComponent({
  name: 'AdminDoc',
  components: { Editor, Toolbar },
  setup() {
    const route = useRoute();
    const param = ref();
    param.value = {};

    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()
    // 内容 HTML
    const valueHtml = ref('<p>小狗水上漂</p>')
    const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: { customRender: 'name' }
      },
      // {
      //   title: '父分类',
      //   key: 'parent',
      //   dataIndex: 'parent',
      // },
      // {
      //   title: '顺序',
      //   dataIndex: 'sort',
      // },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];

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
    }

    /**
     * 模态框--表单
     */
    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref(); //每一条数据的点击按钮表单
    doc.value = {};
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleOk = () => {
      modalLoading.value = true;
      doc.value.content = valueHtml.value;
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

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({ id: 0, name: '无' });
    };

    /**
     * 新增方法
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);
      // 为选择树添加一个"无"
      treeSelectData.value.unshift({ id: 0, name: '无' });
    };


    /**
    * 将某节点及其子孙节点全部置为disabled
    */
    const deleteIds = [];
    const getDelectIds = (treeSelectData, id) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          // console.log("delete", node);
          // 将目标节点设置为disabled
          // node.disabled = true;
          deleteIds.push(id)

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDelectIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDelectIds(children, id);
          }
        }
      }
    };

    /**
    * 删除方法
    */
    const deleteHandle = (ids) => {
      getDelectIds(level1.value, ids);
      axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
        const data = response.data
        if (data.success) {
          //重新加载列表
          handleQuery();
        }
      });
    };


    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData, id) => {
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          // console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };


    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }

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
      modalLoading,
      treeSelectData,


      editorRef,
      valueHtml,
      mode: 'simple', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated
    }
  },
})
</script>



<style>
/*工具栏样式*/
.toolbar {
  border: 1px solid #d9d9d9;
  margin-bottom: 10px;
}

/*工具栏剧中显示*/
.w-e-toolbar {
  justify-content: center !important;
}

/*编辑器样式*/
.editable {
  border: 1px solid #d9d9d9;
  min-height: 800px;
  width: 850px;
  margin: 30px auto 150px auto;
  background-color: #fff;
  box-shadow: 0 2px 10px rgb(0 0 0 / 12%);
  border: 1px solid #e8e8e8;
}
</style>
