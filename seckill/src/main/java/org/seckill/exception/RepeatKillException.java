package org.seckill.exception;

/**
 * �ظ���ɱ�쳣����һ���������쳣������Ҫ�����ֶ�try catch
 * Mysqlֻ֧���������쳣�Ļع�����
 * @author Terence
 *
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}