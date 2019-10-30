package com.cretin.tools.cityselect;

import java.util.List;

/**
 * @date: on 2019-10-29
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class CityResponse {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * areaId : 252
         * name : 安徽
         * sons : [{"areaId":"3116","name":"安庆","sons":null},{"areaId":"3111","name":"蚌埠","sons":null},{"areaId":"3122","name":"亳州","sons":null},{"areaId":"3118","name":"滁州","sons":null},{"areaId":"3123","name":"池州","sons":null},{"areaId":"3119","name":"阜阳","sons":null},{"areaId":"3109","name":"合肥","sons":null},{"areaId":"3112","name":"淮南","sons":null},{"areaId":"3114","name":"淮北","sons":null},{"areaId":"3117","name":"黄山","sons":null},{"areaId":"3121","name":"六安","sons":null},{"areaId":"3113","name":"马鞍山","sons":null},{"areaId":"3115","name":"铜陵","sons":null},{"areaId":"3110","name":"芜湖","sons":null},{"areaId":"3120","name":"宿州","sons":null},{"areaId":"3124","name":"宣城","sons":null}]
         */

        private String areaId;
        private String name;
        private List<SonsBean> sons;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SonsBean> getSons() {
            return sons;
        }

        public void setSons(List<SonsBean> sons) {
            this.sons = sons;
        }

        public static class SonsBean {
            /**
             * areaId : 3116
             * name : 安庆
             * sons : null
             */

            private String areaId;
            private String name;
            private Object sons;

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getSons() {
                return sons;
            }

            public void setSons(Object sons) {
                this.sons = sons;
            }
        }
    }
}
