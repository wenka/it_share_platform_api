package com.wenka.domain.service;

import com.wenka.commons.util.PinYinUtil;
import com.wenka.commons.util.SecureCoder;
import com.wenka.domain.dao.UserDao;
import com.wenka.domain.model.Attachment;
import com.wenka.domain.model.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 用户
 * Created by 文卡<wkwenka@gmail.com> on 2017/4/2.
 */
@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private LogService logService;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    public User get(String id) {
        User user = userDao.get(id);
        return user;
    }

    /**
     * 保存/修改用户
     *
     * @param user
     */
    public void saveOrUpdate(User user) {
        String firstSpell = PinYinUtil.getFirstSpell(user.getName());
        user.setSpell(firstSpell);
        userDao.saveOrUpdate(user);
        logService.save("资料修改生成", user);
    }

    /**
     * 更新用户头像
     *
     * @param user
     */
    public void updateAttachment(User user) {
        String userId = user.getId();
        User _user = userDao.get(userId);

        Attachment attachment = user.getAttachment();
        if (attachment != null) {
            String id = attachment.getId();
            Attachment _attachment = attachmentService.get(id);
            _user.setAttachment(_attachment);
            userDao.update(_user);
        }
    }

    /**
     * 注册
     *
     * @param user
     */
    public void saveSegister(User user) {

        Boolean aBoolean = this.existTel(user.getTel());
        if (aBoolean) {
            throw new RuntimeException("此手机号已经注册");
        }

        user.setName(user.getAccount());
        String firstSpell = PinYinUtil.getFirstSpell(user.getName());
        user.setSpell(firstSpell);
        String securePwd;
        try {
            securePwd = SecureCoder.sha1(user.getPassword());
            user.setPassword(securePwd);
        } catch (NoSuchAlgorithmException e) {
            this.logger.info(e.getMessage());
            throw new RuntimeException("密码无法解析");
        }
        user.setState(Integer.valueOf(1));
        userDao.save(user);
        logService.save("注册", user);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    public void delete(String id) {
        User user = userDao.get(id);
        if (user != null && user.getState().intValue() != -1) {
            user.setState(Integer.valueOf(-1));
            userDao.update(user);
            logService.save("注册", user);
        } else {
            throw new RuntimeException("该账户已删除");
        }
    }

    /**
     * 通过账户与密码获取用户
     *
     * @param account
     * @param password
     * @return
     */
    public User getByAccountPswd(String account, String password) {
        account = StringUtils.trimToEmpty(account);
        password = StringUtils.trimToEmpty(password);

        String securePwd = null;
        try {
            securePwd = SecureCoder.sha1(password);
        } catch (NoSuchAlgorithmException e) {
            this.logger.info(e.getMessage());
            throw new RuntimeException("密码无法解析");
        }

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("account", account));
        criteria.add(Restrictions.eq("password", securePwd));
        criteria.add(Restrictions.ne("state", Integer.valueOf(-1)));
        List list = this.userDao.findByCriteria(criteria);

        if (list != null && list.size() > 0) {
            if (list.size() == 1) {
                User user = (User) list.get(0);
                if (user.getState().intValue() == 0) {
                    throw new RuntimeException("账户被冻结");
                }
                return user;
            } else {
                throw new RuntimeException("账户异常");
            }
        }

        return null;

    }

    /**
     * 通过账户查看用户是否存在
     *
     * @param account
     * @return
     */
    public Boolean exist(String account) {
        account = StringUtils.trimToEmpty(account);
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("account", account));
        detachedCriteria.add(Restrictions.ne("state", Integer.valueOf(-1)));
        List<User> byCriteria = this.userDao.findByCriteria(detachedCriteria);

        if (byCriteria != null && byCriteria.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询电话号是否已被注册
     *
     * @param tel
     * @return
     */
    public Boolean existTel(String tel) {
        tel = StringUtils.trimToEmpty(tel);
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.eq("tel", tel));
        detachedCriteria.add(Restrictions.ne("state", Integer.valueOf(-1)));
        List<User> byCriteria = this.userDao.findByCriteria(detachedCriteria);
        if (byCriteria != null && byCriteria.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 通过账户和电话查询用户
     *
     * @param account
     * @param tel
     * @return
     */
    public User getByaccountAndTel(String account, String tel) {
        account = StringUtils.trimToEmpty(account);
        tel = StringUtils.trimToEmpty(tel);

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("account", account));
        criteria.add(Restrictions.eq("tel", tel));
        criteria.add(Restrictions.ne("state", Integer.valueOf(-1)));
        List list = this.userDao.findByCriteria(criteria);

        if (list != null && list.size() > 0) {
            if (list.size() == 1) {
                User user = (User) list.get(0);
                return user;
            } else {
                throw new RuntimeException("账户异常");
            }
        }

        return null;
    }

    /**
     * 更新密码
     *
     * @param id
     * @param password
     */
    public void updatePswd(String id, String password) {
        User user = this.userDao.get(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        try {
            password = SecureCoder.sha1(password);
        } catch (NoSuchAlgorithmException e) {
            this.logger.info(e.getMessage());
            throw new RuntimeException("密码无法解析");
        }
        user.setPassword(password);
        this.userDao.update(user);
    }

    /**
     * 修改用户积分
     *
     * @param id
     * @param num 更新的积分 取正负值
     */
    public void updateUserIntegral(String id, BigInteger num) {
        User user = this.userDao.get(id);
        BigInteger integral1 = user.getIntegral();
        user.setIntegral(integral1.add(num));
        this.userDao.update(user);
    }

}
