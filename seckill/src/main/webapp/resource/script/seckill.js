//�����Ҫ�����߼���js����
// javascript ģ�黯(package.��.����)

var seckill = {

    //��װ��ɱ���ajax��url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    
    handleSeckill: function (seckillId, node) {
        //��ȡ��ɱ��ַ,������ʾ��,ִ����ɱ
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">��ʼ��ɱ</button>');

        $.get(seckill.URL.exposer(seckillId), {}, function (result) {
            //�ڻص�������ִ�н�������
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //������ɱ
                    //��ȡ��ɱ��ַ
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl: " + killUrl);
                    //��һ�ε���¼�
                    $('#killBtn').one('click', function () {
                        //ִ����ɱ����
                        //1.�Ƚ��ð�ť
                        $(this).addClass('disabled');//,<-$(this)===('#killBtn')->
                        //2.������ɱ����ִ����ɱ
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //��ʾ��ɱ���
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //δ������ɱ(�������ʱƫ��)
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countDown(seckillId, now, start, end);
                }
            } else {
                console.log('result: ' + result);
            }
        });

    },

    
    //��֤�ֻ���
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;//ֱ���ж϶���ῴ�����Ƿ�Ϊ��,�վ���undefine����false; isNaN �����ַ���true
        } else {
            return false;
        }
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            //��ɱ����
            seckillBox.html('��ɱ����!');
        } else if (nowTime < startTime) {
            //��ɱδ��ʼ,��ʱ�¼���
            var killTime = new Date(startTime + 1000);//todo ��ֹʱ��ƫ��
            seckillBox.countdown(killTime, function (event) {
                //ʱ���ʽ
                var format = event.strftime('��ɱ����ʱ: %D�� %Hʱ %M�� %S�� ');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //ʱ����ɺ�ص��¼�
                //��ȡ��ɱ��ַ,������ʵ�߼�,ִ����ɱ
                console.log('______fininsh.countdown');
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //��ɱ��ʼ
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },   


    //����ҳ��ɱ�߼�
    detail: {
        //����ҳ��ʼ��
        init: function (params) {
            //�ֻ���֤�͵�¼,��ʱ����
            //�滮���ǵĽ�������
            //��cookie�в����ֻ���
            var userPhone = $.cookie('userPhone');
            //��֤�ֻ���
            if (!seckill.validatePhone(userPhone)) {
                //���ֻ� �������
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//��ʾ������
                    backdrop: 'static',//��ֹλ�ùر�
                    keyboard: false//�رռ����¼�
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone: " + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //�绰д��cookie(7�����)
                        $.cookie('userPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //��֤ͨ������ˢ��ҳ��
                        window.location.reload();
                    } else {
                        //todo �����İ���Ϣ��ȡ��ǰ���ֵ���
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">�ֻ��Ŵ���!</label>').show(300);
                    }
                });
            }

            //�Ѿ���¼
            //��ʱ����
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //ʱ���ж� ��ʱ����
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result: ' + result);
                    alert('result: ' + result);
                }
            });
        }
    }

    

}