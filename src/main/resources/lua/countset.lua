for i = 1,KEYS[1] do
    redis.call('SET', "lua"..i, "lua"..i)
end
return "OK"