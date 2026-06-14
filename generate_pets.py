import json
import random

# Rarity distribution
rarities = [
    ("N", 10, "普通"),
    ("R", 10, "稀有"),
    ("SR", 10, "史诗"),
    ("SSR", 10, "传说"),
    ("UR", 10, "神话")
]

# Pet name templates
pet_names = {
    "N": ["小石头", "树叶精", "水滴", "风铃", "泥巴", "小蘑菇", "石头人", "小草精", "水精灵", "小火苗"],
    "R": ["火焰犬", "冰晶狐", "雷电鸟", "岩石龟", "风暴鹰", "暗影狼", "光明鹿", "毒蛇", "冰霜熊", "烈焰马"],
    "SR": ["凤凰", "青龙", "白虎", "玄武", "麒麟", "九尾狐", "雷神", "海神", "风神", "火神"],
    "SSR": ["神圣龙", "暗黑凤凰", "混沌兽", "时空精灵", "创世神", "毁灭者", "永恒之树", "命运之轮", "虚空行者", "星界守护者"],
    "UR": ["宇宙之主", "混沌之源", "永恒之光", "虚空之眼", "命运编织者", "时空旅者", "创世之神", "毁灭之王", "生命之树", "死亡之翼"]
}

# Skill templates
skill_templates = {
    "N": ["普通攻击", "防御姿态", "轻微治愈", "小火球", "水枪", "风刃", "土墙", "雷电击", "暗影闪避", "光明护盾"],
    "R": ["火焰喷射", "冰冻射线", "雷电风暴", "岩石崩塌", "龙卷风", "暗影突袭", "圣光治愈", "毒雾", "冰霜新星", "烈焰冲锋"],
    "SR": ["凤凰涅槃", "青龙吐息", "白虎咆哮", "玄武护盾", "麒麟祥瑞", "九尾妖火", "雷霆万钧", "海啸", "狂风暴雨", "地狱火"],
    "SSR": ["神圣审判", "暗黑吞噬", "混沌冲击", "时空扭曲", "创世之光", "毁灭射线", "永恒之根", "命运逆转", "虚空裂缝", "星界风暴"],
    "UR": ["宇宙大爆炸", "混沌之力", "永恒之光", "虚空吞噬", "命运编织", "时空断裂", "创世之力", "毁灭之息", "生命之泉", "死亡之触"]
}

# Story templates
story_templates = {
    "N": ["一只普通的小精灵", "来自森林的居民", "水边的守护者", "风中的旅人", "大地之子"],
    "R": ["拥有特殊能力的精灵", "传说中的生物", "古老的守护者", "神秘的存在", "强大的战士"],
    "SR": ["神话中的神兽", "远古的守护神", "传说中的存在", "强大的神灵", "神秘的生物"],
    "SSR": ["超越凡尘的存在", "创世之初的神灵", "掌控时空的强者", "永恒不灭的生命", "宇宙的守护者"],
    "UR": ["宇宙的创造者", "混沌的化身", "永恒的统治者", "虚空的主宰", "命运的编织者"]
}

def generate_pets():
    pets = []
    pet_id = 1
    
    for rarity, count, rarity_name in rarities:
        for i in range(count):
            name = pet_names[rarity][i]
            skill = skill_templates[rarity][i]
            story = story_templates[rarity][i % len(story_templates[rarity])]
            
            # Generate stats based on rarity
            base_stats = {
                "N": (10, 5, 3, 2),
                "R": (20, 10, 6, 4),
                "SR": (35, 18, 12, 8),
                "SSR": (50, 25, 18, 12),
                "UR": (70, 35, 25, 18)
            }
            
            health, attack, defense, speed = base_stats[rarity]
            
            # Add some randomness
            health = random.randint(health - 5, health + 5)
            attack = random.randint(attack - 2, attack + 2)
            defense = random.randint(defense - 2, defense + 2)
            speed = random.randint(speed - 1, speed + 1)
            
            pet = {
                "id": f"pet_{pet_id:03d}",
                "name": name,
                "rarity": rarity,
                "health": health,
                "attack": attack,
                "defense": defense,
                "speed": speed,
                "skillName": skill,
                "skillDescription": f"{name}使用{skill}，造成大量伤害",
                "story": story,
                "imageFileName": f"pet_{pet_id:03d}.png"
            }
            
            pets.append(pet)
            pet_id += 1
    
    return pets

if __name__ == "__main__":
    pets = generate_pets()
    with open("app/src/main/assets/pets.json", "w", encoding="utf-8") as f:
        json.dump(pets, f, ensure_ascii=False, indent=2)
    print(f"Generated {len(pets)} pets")