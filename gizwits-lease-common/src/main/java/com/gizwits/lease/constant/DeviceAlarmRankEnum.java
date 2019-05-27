package com.gizwits.lease.constant;

public enum DeviceAlarmRankEnum {
        /**
         * 警告级别展示
         */
        RANK_1(1,"级别1"),
        RANK_2(2,"级别2"),
        RANK_3(3,"级别3");

        private  Integer rank;
        private  String value;

        DeviceAlarmRankEnum(Integer rank, String value) {
            this.rank = rank;
            this.value = value;
        }
        public static DeviceAlarmRankEnum get(Integer key){
            if(key == null){
                return null;
            }else {
                DeviceAlarmRankEnum[] arr$ = values();
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                   DeviceAlarmRankEnum appResultKeyEnum = arr$[i$];
                    if(appResultKeyEnum.getRank().equals(key)) {
                        return appResultKeyEnum;
                    }
                }

                return null;
            }
        }

        public static boolean is(DeviceAlarmRankEnum appResultKeyEnum, Integer key) {
            return appResultKeyEnum != null && key != null?appResultKeyEnum.getRank().equals(key):false;
        }


        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }