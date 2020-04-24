import Vue from "vue";
import RowOperation from "./RowOperation";
import TablePagination from "./TablePagination";

const pageComponents = {
  install: function () {
    Vue.component("RowOperation", RowOperation);
    Vue.component("TablePagination", TablePagination);
  }
}

export default pageComponents;
