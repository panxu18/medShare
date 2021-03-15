pragma experimental ABIEncoderV2;
pragma solidity ^0.4.24;

import "./Table.sol";

contract Record {
    // event
    event AddEvent(int256 ret, string[] record);
    event ConfirmEvent(int256 ret, string id, string signed);

    string constant TABLE_NAME = "t_recordV2";

        constructor() public {
            // 构造函数中创建t_record表
            createTable();
        }

        function createTable() private {
            TableFactory tf = TableFactory(0x1001);
            tf.createTable(TABLE_NAME, "id", "type,shared_secret,index,hash,signed,user,user_pk,R,pk,id_commit,superivision_value,h,w1,w2");
        }

        function openTable() private returns(Table) {
            TableFactory tf = TableFactory(0x1001);
            Table table = tf.openTable(TABLE_NAME);
            return table;

        }

    /*
    描述 : 根据id查询电子健康记录是否存在
    参数 ：
            id : 健康数据ID
    返回值：
            存在成功返回0, 电子健康数据不存在-1
    */
    function exist(string id) public constant returns(int256) {
        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(id, table.newCondition());
        if (0 == uint256(entries.size())) {
            return (-1);
        } else {
            return (0);
        }
    }

    /*
    描述 : 根据普通健康数据ID查询电子健康数据元信息
    参数 ：
            id : 健康数据ID

    返回值：
            参数一： 成功返回0, 电子健康数据不存在-1
    */
    function select(string id) public constant returns(int256, int, string[]) {
        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(id, table.newCondition());
        string[] memory record;
        if (0 == uint256(entries.size())) {
            return (-1, 0, record);
        } else {
            Entry entry = entries.get(0);
            if (entry.getInt("type")== 0) {
                record = new string[](8);
            } else {
                record = new string[](13);
            }

            record[0] = id;
            record[1] = entry.getString("type");
            record[2] = entry.getString("shared_secret");
            record[3] = entry.getString("index");
            record[4] = entry.getString("hash");
            record[5] = entry.getString("signed");
            if (entry.getInt("type")== 0) {
                record[6] = entry.getString("user");
                record[7] = entry.getString("user_pk");
            } else {
                record[6] = entry.getString("R");
                record[7] = entry.getString("pk");
                record[8] = entry.getString("id_commit");
                record[9] = entry.getString("superivision_value");
                record[10] = entry.getString("h");
                record[11] = entry.getString("w1");
                record[12] = entry.getString("w2");
            }


            return (0, entry.getInt("type"), record);
        }
    }

    /*
    描述 : 创建健康数据元信息
    参数 ：
            id : 健康记录id
    返回值：
            0  创建成功
            -1 健康数据已存在
            -2 其他错误
    */
    function create(int item_type, string[] record) public returns(int256){
        int256 ret_code = 0;
        int256 ret= 0;

        // 查询健康数据是否存在
        ret = exist(record[0]);

        if(ret != 0) {
            Table table = openTable();
            Entry entry = table.newEntry();
            entry.set("id", record[0]);
            entry.set("type", record[1]);
            entry.set("shared_secret", record[2]);
            entry.set("index", record[3]);
            entry.set("hash", record[4]);
            entry.set("signed", record[5]);
            if (item_type == 0) {
                entry.set("user", record[6]);
                entry.set("user_pk", record[7]);
            } else {
                entry.set("R", record[6]);
                entry.set("pk", record[7]);
                entry.set("id_commit", record[8]);
                entry.set("superivision_value", record[9]);
                entry.set("h", record[10]);
                entry.set("w1", record[11]);
                entry.set("w2", record[12]);
            }
            // 插入
            int count = table.insert(record[0], entry);
            if (count == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        } else {
            // 账户已存在
            ret_code = -1;
        }
        emit AddEvent(ret_code, record);
        return ret_code;
    }

    /*
    描述 : 健康数据确认
    参数 ：
            id : 健康数据ID
            signed ： 用户确认签名
    返回值：
            0  健康数据确认成功
            -1 电子健康数据不存在
            -2 其他错误
    */
    function confirm(string id, string signed) public returns(int256) {
        // 查询健康数据信息
        int ret_code = 0;
        int256 ret = 0;

        // 查询健康数据是否存在
        ret = exist(id);

        // (ret, item_id，item_type,shared_secret,index,hash,item_signed,user,user_pk,R,pk,id_commit,superivision_value,h,w1,w2) = select(id);
        if(ret != 0) {
            ret_code = -1;
            // 转移账户不存在
            return ret_code;

        }

        Table table = openTable();

        Entry entry = table.newEntry();
        entry.set("signed", signed);
        // 更新健康数据
        int count = table.update(id, entry, table.newCondition());
        if(count != 1) {
            ret_code = -5;
            // 失败? 无权限或者其他错误?
            return ret_code;
        }
        emit ConfirmEvent(ret_code, id, signed);
        return ret_code;
    }

}
