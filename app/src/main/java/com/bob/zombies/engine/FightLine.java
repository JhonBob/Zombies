package com.bob.zombies.engine;



import com.bob.zombies.baseinterface.AttackPlant;
import com.bob.zombies.baseinterface.BaseElement;
import com.bob.zombies.baseinterface.Bullet;
import com.bob.zombies.baseinterface.Plant;
import com.bob.zombies.baseinterface.Zombies;

import org.cocos2d.actions.CCScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//行战场（模型）
public class FightLine {
    //行号
    private int lineNum;
    //记录僵尸
    ArrayList<Zombies> zombiesList=new ArrayList<>();
    //记录植物
    Map<Integer,Plant> plants=new HashMap<>();
    // 创建了一个攻击类型的植物的集合
    List<AttackPlant> attackPlants=new ArrayList<AttackPlant>();

    public FightLine(int lineNum){
        super();
        this.lineNum=lineNum;
        //定时器控制
        CCScheduler.sharedScheduler().schedule("attactPlant", this, 0.2f, false);
        CCScheduler.sharedScheduler().schedule("creatPease", this, 0.5f, false);
        CCScheduler.sharedScheduler().schedule("attackZombies", this, 0.1f, false);
    }

    //行战场添加僵尸记录
    public void addZombies(final Zombies zombies){
        zombiesList.add(zombies);
        zombies.setDieListener(new BaseElement.DieListener() {

            @Override
            public void die() {
                zombiesList.remove(zombies);// 当僵尸死亡的时候 移除集合
            }
        });

    }

    //加植物
    public void addPlant(final Plant plant){
        plants.put(plant.getRow(), plant);
        plant.setDieListener(new BaseElement.DieListener() {
            // 当植物死亡的时候调用
            @Override
            public void die() {
                plants.remove(plant.getRow());// 当植物死亡的时候 把它在集合中移除出去
                if (plant instanceof AttackPlant) {
                    attackPlants.remove(plant);//  当植物如果是攻击类型的植物 死亡的时候 把他在攻击类型的植物中移除
                }
            }
        });
        //攻击类型的植物添加
        if(plant instanceof AttackPlant){
            attackPlants.add((AttackPlant)plant);
        }
    }

    //判断列上是否有植物
    public boolean hasPlant(Plant plant){
        return plants.containsKey(plant.getRow());
    }

    //僵尸攻击植物
    public void  attactPlant(float t) {
        //有僵尸有植物
        if (plants.size() > 0 && zombiesList.size() > 0) {
            //僵尸攻击植物
            for (Zombies zombies : zombiesList) {
                int row = (int) (zombies.getPosition().x / 46);  // 获取到僵尸所在的列
                row = row - 1;
                Plant plant = plants.get(row);  // 根据列号获取到列上的植物
                if (plant != null) {  //植物不为空
                    zombies.attack(plant);// 攻击植物
                }
            }
        }
    }

    //植物攻击僵尸
    public void attackZombies(float t){
        if(zombiesList.size()>0&&attackPlants.size()>0){
            //  遍历所有僵尸 观察僵尸的x坐标的范围

            for(Zombies zombies:zombiesList){
                float x=zombies.getPosition().x;
                float left=x-20;
                float right=x+20;
                // 遍历所有的攻击类型植物
                for(AttackPlant attackPlant:attackPlants){
                    List<Bullet> bullets = attackPlant.getBullets();
                    for(Bullet bullet:bullets){
                        float bulletX=bullet.getPosition().x;
                        if(bulletX>left&&bulletX<right){
                            //子弹在僵尸的范围之中  产生了碰撞
                            zombies.attacked(bullet.getAttack());// 僵尸被攻击
                            bullet.setAttack(0);// 子弹的攻击力 变为0
                            bullet.setVisible(false);// 子弹不可见了
                        }
                    }
                }
            }
        }
    }

    //创建子弹
    public void creatPease(float t){
        //  保证当前行 必须有攻击的植物   //  保证当前的行  必须有僵尸
        if(attackPlants.size()>0&& zombiesList.size()>0){
            for(AttackPlant attackPlant:attackPlants){
                attackPlant.createBullet();//创建子弹
            }
        }
    }
}

